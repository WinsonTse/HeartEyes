package io.github.winsontse.hearteyes.page.image.presenter;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.image.contract.GalleryContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;
import rx.functions.Action1;

public class GalleryPresenter extends BasePresenterImpl implements GalleryContract.Presenter {
    private GalleryContract.View view;

    @Inject
    public GalleryPresenter(final GalleryContract.View view) {
        this.view = view;
    }
}
