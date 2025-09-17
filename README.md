# Tracking Anime Document

## Yêu cầu

- **Maven**: 3.9.x
- **JavaJDK**: 24.0.2

## Setup

1. Clone về máy
2. Sửa file `.env.example` thành `.env`, điền thông tin  connection của mình
3. Với lần chạy đầu tiên, mở khóa các comment trong function `seeds` của `SeedData.java` để tự tạo dữ liễu mẫu
4. Build và Run
    - `mvn clean javafx:run` : chạy
    - `mvn clean install" - build lại


**File hình**:
- lưu trong:
### Controls

1. JavaFX,
2. JFoenix
3. ControlsFX

## kiên trúc MVVM

- ViewModel: logic (gọi services), Property(state) + Event
- Model:
- View:
    - Controller: binding ViewModel, navigation
**Thứ tự:** Model -> ViewModel -> Controller

## Màn hình (Views)

## Auth
- Login,
- Register

### User
- MyList
- Notification
- Profile
- Dashboard/Overview
- Discover (List Anime, Search)
- AnimeDetails

### Admin
- AdminDashboard
- AnimeManagement
- UserManagement

