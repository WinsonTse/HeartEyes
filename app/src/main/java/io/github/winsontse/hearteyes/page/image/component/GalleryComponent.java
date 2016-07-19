package io.github.winsontse.hearteyes.page.image.component;

import io.github.winsontse.hearteyes.page.image.GalleryFragment;
import io.github.winsontse.hearteyes.page.image.module.GalleryModule;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {GalleryModule.class})
public interface GalleryComponent {

    void inject(GalleryFragment fragment);

}

