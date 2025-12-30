import SwiftUI
import sharedIntegrationBase

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
