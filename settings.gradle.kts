include(":lib")

fun devInclude(id: String) {
    if (!extra.has("dev.$id")) {
        include(":$id")
        project(":$id").projectDir = File("../$id/lib")
    }
}

devInclude("eb")
devInclude("ebui")

include(":java")
