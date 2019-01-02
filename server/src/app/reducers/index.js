import {
    UPDATE_NEW_DIR,
    SET_SYNC_DIR,
    SAVING_SETTINGS,
    SAVED_SETTINGS,
} from '../actions/index';

export default (state, action) => {
    switch (action.type) {
        case UPDATE_NEW_DIR:
            return {
                ...state,
                newDir: action.data,
            };
        case SET_SYNC_DIR:
            return {
                ...state,
                syncDir: action.data,
            };
        case SAVING_SETTINGS:
            return {
                ...state,
                savingSettings: true,
            };
        case SAVED_SETTINGS:
            return {
                ...state,
                savingSettings: false,
            };
        default:
            return state;
    }
};
