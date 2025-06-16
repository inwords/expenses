import SwiftUI
import shared_integration_base

@main
struct iOSApp: App {

    init() {
        RegisterComponentsKt.registerComponents()
        EnableSyncKt.enableSync()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .ignoresSafeArea()
        }
    }
}
