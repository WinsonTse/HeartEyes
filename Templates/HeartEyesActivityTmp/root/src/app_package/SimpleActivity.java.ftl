package ${packageName};

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import javax.inject.Inject;
import android.support.annotation.Nullable;
import ${packageName}.component.Dagger${pageName}Component;
import ${packageName}.contract.${pageName}Contract;
import ${packageName}.module.${pageName}Module;
import ${packageName}.presenter.${pageName}Presenter;
import ${applicationPackage}.app.AppComponent;
import ${applicationPackage}.page.base.BaseActivity;
import ${applicationPackage}.page.base.BasePresenter;
import ${applicationPackage}.R;

public class ${pageName}Activity extends BaseActivity implements ${pageName}Contract.View {

    @Inject
    ${pageName}Presenter presenter;

    @Override
    public void initView(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.${layoutName};
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