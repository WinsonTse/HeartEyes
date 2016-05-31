package io.github.winsontse.hearteyes.page.qrcode.module;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.page.qrcode.contract.ScannerContract;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;


@Module
public class ScannerModule {
    private ScannerContract.View view;

    public ScannerModule(ScannerContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    ScannerContract.View provideScannerView() {
        return view;
    }
}
