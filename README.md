# Tracking Anime Document

## Giới thiệu

Dự án tracking anime cho phép người dùng kìm tiếm anime từ Jikan API sau đó thực hiện chức năng tracking như: lên lịch, theo dõi lịch hằng tuần và nhận thông báo khi đến thời gian chiếu.

## Yêu cầu cài đặt phần mềm

### Môi trường
- **Maven**: 3.9.x
- **JavaJDK**: 24.0.2

## Setup dự án
1. Clone dự án từ github
2. Sửa file `.env.example` thành `.env`, điền thông tin  connection của mình
3. Build và Run
    - `mvn clean javafx:run` : chạy
    - `mvn clean install` - build lại
    - `mvn clean package` - export .jar

**Nơi lưu trữ File hình**: `$HOME/.tracking-anime/images/`

## Controls UI library

1. JavaFX
2. JFoenix -> đã cũ (bỏ)
3. ControlsFX

## kiên trúc MVVM

- ViewModel: logic (gọi services), Property(state) + Event
- Model:
- View:
    - Controller: binding ViewModel, navigation
**Thứ tự:** Model -> ViewModel -> Controller

## Màn hình (Views)

- Dashboard (Home)
    1. Overview tab
    2. Discover tab
    3. My List tab
- Notification