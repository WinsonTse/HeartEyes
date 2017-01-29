package io.github.winsontse.hearteyes.page.qrcode.component;

import io.github.winsontse.hearteyes.page.qrcode.ScannerActivity;
import io.github.winsontse.hearteyes.page.qrcode.module.ScannerModule;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {ScannerModule.class})
public interface ScannerComponent {

    void inject(ScannerActivity fragment);

}

