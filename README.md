# FREX - Rendering Extensions for the Fabric Rendering API

Extensions for the Fabric rendering API.  These may or may not be proposed and accepted for inclusion in the Fabric API.  

Also includes a shaded copy of [JOML](https://github.com/JOML-CI/JOML) for use by renderer implementations and rendering mods.

Packaged as a separate mod so that rendering implementations and mods that consume these extensions can
depend on it without directly depending on specific implementation.

More information on using FREX is available on the [Renderosity Wiki](https://github.com/grondag/renderosity/wiki).

# Using FREX

Add the maven repo where my libraries live to your build.gradle

```gradle
repositories {
    maven {
    	name = "dblsaiko"
    	url = "https://maven.dblsaiko.net/"
    }
}
```

And add FREX to your dependencies

```gradle
dependencies {
	modCompile "grondag:frex:0.7.+"
	include "grondag:frex:0.7.+"
}
```

The ```include``` is not necessary if you are depending on another mod that also includes FREX.  Currently, [Canvas](https://github.com/grondag/canvas) and [JMX](https://github.com/grondag/json-model-extensions) both include FREX.

Note that version is subject to change - look at the repo to find latest.
