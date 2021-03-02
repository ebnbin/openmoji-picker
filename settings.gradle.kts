include(":lib")

fun devInclude(id: String) {
    if (!extra.has("devLib.$id")) {
        include(":$id")
        project(":$id").projectDir = File("../$id/lib")
    }
}

devInclude("eb")
devInclude("eb-material")
