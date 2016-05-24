package ${packageName}.presenter;

import javax.inject.Inject;
import ${packageName}.contract.${pageName}Contract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;

public class ${pageName}Presenter extends BasePresenterImpl implements ${pageName}Contract.Presenter {
    private ${pageName}Contract.View view;

    @Inject
    public ${pageName}Presenter(${pageName}Contract.View view) {
        this.view = view;
    }
}
