package ${packageName}.component;

import ${packageName}.${fragmentName};
import ${packageName}.module.${pageName}Module;
import ${applicationPackage}.app.AppComponent;
import ${applicationPackage}.util.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = {${pageName}Module.class})
public interface ${pageName}Component {

    void inject(${pageName}Activity fragment);

}

