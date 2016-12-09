package io.github.winsontse.hearteyes.page.image;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.data.model.ImageEntity;
import io.github.winsontse.hearteyes.page.adapter.ImagePickerAdaper;
import io.github.winsontse.hearteyes.page.adapter.base.BaseRecyclerAdapter;
import io.github.winsontse.hearteyes.util.FileUtil;
import io.github.winsontse.hearteyes.util.HeartEyesSubscriber;
import io.github.winsontse.hearteyes.util.TimeUtil;
import io.github.winsontse.hearteyes.util.UIUtil;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 打算以后项目也可以使用,所以使用activity实现,且未实现MVP
 */
public class ImagePickerActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ImagePickerAdaper adaper;
    private static final String ORDER = "DESC";
    private static final int PADDING = 4;
    public static final int DEFUALT_MAX_COUNT = 15;
    public static final int REQUEST_PICKER_IMAGE = 1034;
    private static final int REQUEST_CAMERA = 1035;
    private File currentTempImageFile;


    private static final String EXTRA_MAX_COUNT = "max_count";
    private static final String EXTRA_SELECTED_IMAGE_LIST = "selected_image_list";

    private CompositeSubscription compositeSubscription;
    private Resources resources;
    private int maxCount;
    private int colorWhite;
    private int colorPink;

    private MenuItem okMenu;
    private ArrayList<ImageEntity> selectedList;


    public static final String[] PROJECTIONS = {
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
    };


    public static void startImagePicker(Fragment fragment, int count, List<ImageEntity> selectedList) {
        Intent intent = new Intent(fragment.getActivity(), ImagePickerActivity.class);
        intent.putExtra(EXTRA_MAX_COUNT, count);
        if (selectedList != null) {
            intent.putParcelableArrayListExtra(EXTRA_SELECTED_IMAGE_LIST, (ArrayList<ImageEntity>) selectedList);
        }
        fragment.startActivityForResult(intent, REQUEST_PICKER_IMAGE);
    }

    public static void startImagePicker(Activity activity, int count, List<ImageEntity> selectedList) {
        Intent intent = new Intent(activity, ImagePickerActivity.class);
        intent.putExtra(EXTRA_MAX_COUNT, count);
        if (selectedList != null) {
            intent.putParcelableArrayListExtra(EXTRA_SELECTED_IMAGE_LIST, (ArrayList<ImageEntity>) selectedList);
        }
        View view = activity.findViewById(R.id.tv_image_count);
        ActivityCompat.startActivityForResult(activity, intent, REQUEST_PICKER_IMAGE,
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle());
//        activity.startActivityForResult(intent, REQUEST_PICKER_IMAGE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setEnterTransition(new Slide(Gravity.BOTTOM));
        getWindow().setExitTransition(null);

        setContentView(R.layout.activity_image_picker);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        maxCount = intent.getIntExtra(EXTRA_MAX_COUNT, DEFUALT_MAX_COUNT);
        selectedList = intent.getParcelableArrayListExtra(EXTRA_SELECTED_IMAGE_LIST);

        resources = getResources();
        colorPink = resources.getColor(R.color.md_pink_100);
        colorWhite = resources.getColor(R.color.md_white);
        initToolbar();
        initAdapter();
        requestPermission();

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAfterTransition();
            }
        });
    }

    private void initAdapter() {
        final int padding = UIUtil.dpToPx(this, PADDING);

        adaper = new ImagePickerAdaper();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adaper);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(padding, padding, padding, padding);
            }
        });
        adaper.setMaxCount(maxCount);
        adaper.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int size = adaper.getSelectedList().size();
                updateTitle(size);
                okMenu.setVisible(size > 0);
            }
        });

        if (selectedList != null && selectedList.size() > 0) {
            updateTitle(selectedList.size());
            adaper.getSelectedList().addAll(selectedList);
        }
    }

    private void updateTitle(int size) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resources.getString(R.string.tips_selected_images_count, size) + maxCount);
            toolbar.setTitleTextColor(size == maxCount ? colorPink : colorWhite);
        }
    }

    private void addSubscription(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    private void requestPermission() {
        RxPermissions
                .getInstance(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            loadImages();
                        } else {
                            showToast(getString(R.string.not_permission));
                        }
                    }
                });
    }


    private void loadImages() {

        Subscription subscribe = getCursorObservable(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                .map(new Func1<Cursor, List<ImageEntity>>() {
                    @Override
                    public List<ImageEntity> call(Cursor cursor) {
                        List<ImageEntity> result = new ArrayList<>();
                        while (cursor.moveToNext()) {
                            result.add(cursorToImageEntity(cursor));
                        }
                        cursor.close();
                        return result;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HeartEyesSubscriber<List<ImageEntity>>() {
                    @Override
                    public void onNext(List<ImageEntity> imageList) {
                        adaper.addItems(imageList);
                    }

                    @Override
                    public void handleError(Throwable e) {
                        showToast(getString(R.string.cannot_load_local_images));
                    }
                });
        addSubscription(subscribe);
    }

    @NonNull
    private ImageEntity cursorToImageEntity(Cursor cursor) {
        ImageEntity entity = new ImageEntity();
        entity.setDateAdded(cursor.getLong(cursor.getColumnIndex(PROJECTIONS[0])));
        entity.setDateModified(cursor.getLong(cursor.getColumnIndex(PROJECTIONS[1])));
        entity.setData(cursor.getString(cursor.getColumnIndex(PROJECTIONS[2])));
        entity.setSize(cursor.getLong(cursor.getColumnIndex(PROJECTIONS[3])));
        entity.setDisplayName(cursor.getString(cursor.getColumnIndex(PROJECTIONS[4])));
        entity.setTitle(cursor.getString(cursor.getColumnIndex(PROJECTIONS[5])));
        entity.setMineType(cursor.getString(cursor.getColumnIndex(PROJECTIONS[6])));
        entity.setWidth(cursor.getInt(cursor.getColumnIndex(PROJECTIONS[7])));
        entity.setHeight(cursor.getInt(cursor.getColumnIndex(PROJECTIONS[8])));
        return entity;
    }

    private Observable<Cursor> getCursorObservable(final Uri mediaUri) {
        return Observable
                .create(new Observable.OnSubscribe<Cursor>() {
                    @Override
                    public void call(Subscriber<? super Cursor> subscriber) {
                        Cursor cursor = getContentResolver().query(
                                mediaUri,
                                new String[]{
                                        PROJECTIONS[0],
                                        PROJECTIONS[1],
                                        PROJECTIONS[2],
                                        PROJECTIONS[3],
                                        PROJECTIONS[4],
                                        PROJECTIONS[5],
                                        PROJECTIONS[6],
                                        PROJECTIONS[7],
                                        PROJECTIONS[8]
                                },
                                null,
                                null,
                                PROJECTIONS[1] + " " + ORDER);
                        subscriber.onNext(cursor);
                    }
                });
    }

    private void showToast(String msg) {
        Toast.makeText(ImagePickerActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_picker, menu);
        okMenu = menu.findItem(R.id.action_ok);
        okMenu.setVisible(selectedList != null && selectedList.size() > 0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ok:
                Intent intent = new Intent();
                List<ImageEntity> selectedList = adaper.getSelectedList();
                if (selectedList != null && selectedList.size() > 0) {
                    intent.putExtra(EXTRA_SELECTED_IMAGE_LIST, (ArrayList<ImageEntity>) selectedList);
                    setResult(RESULT_OK, intent);
                }
                finishAfterTransition();
                break;

            case R.id.action_camera:

                openCamera();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openCamera() {
        RxPermissions
                .getInstance(this)
                .request(Manifest.permission.CAMERA)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            if (currentTempImageFile == null) {
                                currentTempImageFile = FileUtil.createTempExternalImageFile(ImagePickerActivity.this, "hearteyes_" + TimeUtil.getFormatTime(System.currentTimeMillis(), "yyyyMMdd_HH_mm_ss_SSS") + ".jpg");
                            }
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            ContentValues contentValues = new ContentValues(1);
                            contentValues.put(MediaStore.Images.Media.DATA, currentTempImageFile.getAbsolutePath());
                            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            cameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                            startActivityForResult(cameraIntent, REQUEST_CAMERA);

                        } else {
                            showToast(getString(R.string.not_permission));
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    handleCameraResult();
                    break;
            }
        }
    }

    private void handleCameraResult() {
        String tmpPath = currentTempImageFile.getAbsolutePath();
        currentTempImageFile = null;
        MediaScannerConnection.scanFile(this, new String[]{tmpPath}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {
                Subscription subscribe = getCursorObservable(uri)
                        .map(new Func1<Cursor, ImageEntity>() {
                            @Override
                            public ImageEntity call(Cursor cursor) {
                                cursor.moveToFirst();
                                ImageEntity imageEntity = cursorToImageEntity(cursor);
                                cursor.close();
                                return imageEntity;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new HeartEyesSubscriber<ImageEntity>() {
                            @Override
                            public void onNext(ImageEntity imageEntity) {
                                adaper.getSelectedList().add(imageEntity);
                                adaper.getData().add(0, imageEntity);
                                adaper.notifyDataSetChanged();
                                okMenu.setVisible(true);

                            }

                            @Override
                            public void handleError(Throwable e) {
                                showToast(getString(R.string.cannot_load_local_images));
                            }
                        });
                addSubscription(subscribe);
            }
        });
    }

    public static List<ImageEntity> handleSelectedImageResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null && data.hasExtra(EXTRA_SELECTED_IMAGE_LIST)) {
            return data.getParcelableArrayListExtra(EXTRA_SELECTED_IMAGE_LIST);
        }
        return null;
    }
}
