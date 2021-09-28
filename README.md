# THE FREX PROJECT IS NOW PART OF VRAM-GUILD 
**THIS REPO IS NO LONGER MAINTAINED.**

**Please go to https://github.com/vram-guild/frex**

# FREX - Rendering Extensions for Minecraft Mods

FREX currently support the Fabric API and Mod Loader.  

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
