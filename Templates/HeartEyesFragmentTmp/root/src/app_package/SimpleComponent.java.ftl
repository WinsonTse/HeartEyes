package ${packageName}.component;

import ${packageName}.${fragmentName};
import ${packageName}.module.${pageName}Module;
import ${applicationPackage}.page.base.ActivityComponent;
import ${applicationPackage}.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {ActivityComponent.class}, modules = {${pageName}Module.class})
public interface ${pageName}Component {

    void inject(${pageName}Fragment fragment);

}

