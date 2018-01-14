export const SET_SYNC_DIR = 'SET_SYNC_DIR';
export const UPDATE_NEW_DIR = 'UPDATE_NEW_DIR';

export const SAVE_SETTINGS = 'SAVE_SETTINGS';
export const SAVING_SETTINGS = 'SAVING_SETTINGS';
export const SAVED_SETTINGS = 'SAVED_SETTINGS';

export function fetchAction(action, data) {
    return {
        type: action,
        data,
    };
};

export function interfaceAction(action, data) {
    return {
        type: action,
        data,        
    };
};
