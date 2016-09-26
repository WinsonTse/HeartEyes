package ${packageName}.module;

import dagger.Module;
import dagger.Provides;
import ${packageName}.contract.${pageName}Contract;
import ${applicationPackage}.util.scope.ActivityScope;


@Module
public class ${pageName}Module {
    private ${pageName}Contract.View view;

    public ${pageName}Module(${pageName}Contract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ${pageName}Contract.View provide${pageName}View() {
        return view;
    }
}
