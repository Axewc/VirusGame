// swift-tools-version: 5.9
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "CyberSystems",
    platforms: [
        .iOS(.v17)
    ],
    products: [
        .library(
            name: "CyberSystems",
            targets: ["CyberSystems"])
    ],
    targets: [
        .target(
            name: "CyberSystems",
            path: "."
        ),
        .testTarget(
            name: "CyberSystemsTests",
            dependencies: ["CyberSystems"],
            path: "Tests"
        )
    ]
)
