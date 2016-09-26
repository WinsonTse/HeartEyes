<?xml version="1.0"?>
<recipe>
    <#include "../common/recipe_manifest.xml.ftl" />

    <instantiate from="root/src/app_package/SimpleActivity.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${activityClass}.java" />
    <instantiate from="root/src/app_package/SimpleContract.java.ftl"
                       to="${escapeXmlAttribute(srcOut)}/contract/${pageName}Contract.java" />
    <instantiate from="root/src/app_package/SimplePresenter.java.ftl"
                       to="${escapeXmlAttribute(srcOut)}/presenter/${pageName}Presenter.java" />
    <instantiate from="root/src/app_package/SimpleComponent.java.ftl"
                       to="${escapeXmlAttribute(srcOut)}/component/${pageName}Component.java" />
    <instantiate from="root/src/app_package/SimpleModule.java.ftl"
                       to="${escapeXmlAttribute(srcOut)}/module/${pageName}Module.java" />

    <instantiate from="root/res/layout/activity_simple.xml.ftl"
                       to="${escapeXmlAttribute(resOut)}/layout/${escapeXmlAttribute(layoutName)}.xml" />
</recipe>
