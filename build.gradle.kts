plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.androidApplication) apply false
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.layout.buildDirectory)
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}
