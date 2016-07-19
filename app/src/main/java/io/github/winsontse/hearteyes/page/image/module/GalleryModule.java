package io.github.winsontse.hearteyes.page.image.module;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.page.image.contract.GalleryContract;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;


@Module
public class GalleryModule {
    private GalleryContract.View view;

    public GalleryModule(GalleryContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    GalleryContract.View provideGalleryView() {
        return view;
    }
}
