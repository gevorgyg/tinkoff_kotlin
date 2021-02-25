package main

import TextEditor
import MediaGallery
import IDE


fun main() {

    // Creating objects
    val vim = TextEditor("Vim", hashSetOf("modular", "extensible"))

    val windowsMediaViewer = MediaGallery(
            "Windows Media Viewer",
            "jpg", hashSetOf("ease of use")
    )

    val idea = IDE("IntelliJ IDEA", "Java", hashSetOf("cool autocompletion", "convenience"))


    // TextEditor functionality
    vim.about()
    println(vim.makeCoolEdit("I like ice cream"))
    vim.feature()
    vim.feature("modular")
    vim.feature("easy for newcomers")
    println()

    // MediaGallery functionality
    windowsMediaViewer.about()
    println(windowsMediaViewer.shufflePeopleOnPhoto(listOf("Vasya", "Petya", "Katya", "Sasha", "Andrei")))
    println()

    // IDE functionality
    idea.about()
    idea.showAnnoyingError()
}