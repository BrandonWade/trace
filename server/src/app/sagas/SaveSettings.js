import { takeLatest, put } from 'redux-saga/effects';
import {
    fetchAction,
    SAVE_SETTINGS,
    SAVING_SETTINGS,
    SAVED_SETTINGS,
} from '../actions/index';

function* saveSettings(action) {
    yield put(fetchAction(SAVING_SETTINGS));

    const headers = new Headers({
        'Content-Type': 'application/json',
    });

    fetch('/settings/update/dir', {
        method: 'POST',
        headers,
        body: JSON.stringify(action.data),
    })
        .then(res => res.json())
        .then(() => put(fetchAction(SAVED_SETTINGS)));
}

export default function* watchSaveSettings() {
    yield takeLatest(SAVE_SETTINGS, saveSettings);
}
