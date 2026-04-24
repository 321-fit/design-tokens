// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "FitUI",
    platforms: [.iOS(.v16)],
    products: [
        .library(name: "FitUI", targets: ["FitUI"]),
    ],
    targets: [
        .target(
            name: "FitUI",
            path: "Sources/FitUI"
        ),
    ]
)
