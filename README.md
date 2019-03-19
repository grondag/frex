# FREX - Fabric Rendering API Extensions

Extensions to the Fabric rendering API.  These may or may not be proposed and accepted for inclusion in the Fabric API.  

Also includes a shaded copy of [JOML](https://github.com/JOML-CI/JOML) for use by renderer implementations and rendering mods.

Packaged as a separate mod so that rendering implementations and mods that consume these extensions can
depend on it without directly depending on specific implementation.

# Using FREX

Add my maven repo to your build.gradle

```
repositories {
    maven {
    	name = "grondag"
    	url = "https://grondag-repo.appspot.com"
    	credentials {
            username "guest"
            password ""
		}
    }
}
```

And add FREX to your dependencies

```
dependencies {
	modCompile "grondag:frex:0.1.69-alpha"
}
```

Note that version is subject to change - look at the repo to find latest.
