<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="app.version" content="${g.meta(name:'app.version')}"/>
    <meta name="app.build" content="${g.meta(name:'app.build')}"/>
    <meta name="description" content="Atlas of Living Australia"/>
    <meta name="author" content="Atlas of Living Australia">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="http://www.ala.org.au/wp-content/themes/ala2011/images/favicon.ico" rel="shortcut icon"  type="image/x-icon"/>

    <title><g:layoutTitle /></title>

    <%-- Do not include JS & CSS files here - add them to your app's "application" module (in "Configuration/ApplicationResources.groovy") --%>
    <r:require modules="bootstrap"/>

    <r:layoutResources/>
    <g:layoutHead />
</head>
<body class="${pageProperty(name:'body.class')}" id="${pageProperty(name:'body.id')}" onload="${pageProperty(name:'body.onload')}">
<g:set var="fluidLayout" value="${pageProperty(name:'meta.fluidLayout')?:grailsApplication.config.skin?.fluidLayout}"/>
<g:set var="containerType" value="${fluidLayout ? 'container-fluid' : 'container'}"/>

<!-- Header -->
<hf:banner logoutUrl="${g.createLink(controller:"logout", action:"logout", absolute: true)}" />
<!-- End header -->

<!-- Container -->
<div class="${containerType}" id="main">
    <g:layoutBody />
</div><!-- End container #main  -->

<!-- Footer -->
<hf:footer/>
<!-- End footer -->

<!-- JS resources-->
<r:layoutResources/>
<script type="text/javascript">
    $(document).ready(function() {
        $("footer div.ftr-secondary").before("<div class=\"row-fluid ftr-ipa\"> \
          <div class=\"span4\"> <div class=\"row-fluid\"> <div class=\"span12\"> <img class=\"img-responsive ipa\" src=\"images/IPAust_stacked_flat_black.jpg\"> </div> </div> </div> \
          <div class=\"span8\"> <p class=\"lead-ipa\">IP Australia administers legislation that governs four types of IP rights: Patents, Trade marks, Designs and Plant breeder's rights</p> </div> \
         </div>"
        );
    });
</script>
</body>
</html>
