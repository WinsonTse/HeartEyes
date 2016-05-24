<?xml version="1.0"?>
<recipe>

    <instantiate from="root/src/app_package/SimpleFragment.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${fragmentName}.java" />
    <instantiate from="root/src/app_package/SimpleContract.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/contract/${pageName}Contract.java" />
    <instantiate from="root/src/app_package/SimplePresenter.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/presenter/${pageName}Presenter.java" />
    <instantiate from="root/src/app_package/SimpleComponent.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/component/${pageName}Component.java" />
    <instantiate from="root/src/app_package/SimpleModule.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/module/${pageName}Module.java" />

    <instantiate from="root/res/layout/fragment_simple.xml.ftl"
                       to="${escapeXmlAttribute(resOut)}/layout/${escapeXmlAttribute(layoutName)}.xml" />

    <open file="${escapeXmlAttribute(resOut)}/layout/${escapeXmlAttribute(layoutName)}.xml" />

</recipe>
