plugins{
    alias(libs.plugins.typst)
}


typst {
    version = "v0.14.2"
}

val main by typst.sourceSets.registering {
    root.set(layout.projectDirectory)
    documents.add("../index")
}
