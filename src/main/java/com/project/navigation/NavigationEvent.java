package com.project.navigation;

public enum NavigationEvent {
    LOGIN_SUCCESS,

    // navigation
    NAVIGATE_TO_LOGIN,
    NAVIGATE_TO_REGISTER,
    NAVIGATE_TO_USER_DASHBOARD,
    NAVIGATE_TO_ADMIN_DASHBOARD,
    NAVIGATE_BACK,

    // flag
    FLAT_NEED_LOGIN,
    FLAT_SUCCESS,
    FLAT_FAILURE,
    // popup
    CONFIRM_LOGOUT_POPUP,
    CONFIRM_DELETE_POPUP,
}
