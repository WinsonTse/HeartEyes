package ${packageName};

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import javax.inject.Inject;
import ${packageName}.component.Dagger${pageName}Component;
import ${packageName}.contract.${pageName}Contract;
import ${packageName}.module.${pageName}Module;
import ${packageName}.presenter.${pageName}Presenter;
import ${applicationPackage}.app.AppComponent;
import ${applicationPackage}.page.base.BaseFragment;
import ${applicationPackage}.page.base.BasePresenter;
import ${applicationPackage}.R;

public class ${pageName}Fragment extends BaseFragment implements ${pageName}Contract.View {

    public static ${pageName}Fragment newInstance() {
        Bundle args = new Bundle();
        ${pageName}Fragment fragment = new ${pageName}Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    ${pageName}Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.${layoutName}, container, false);
        return rootView;
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        Dagger${pageName}Component.builder()
                .appComponent(appComponent)
                .${pageName?uncap_first}Module(new ${pageName}Module(this))
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

}