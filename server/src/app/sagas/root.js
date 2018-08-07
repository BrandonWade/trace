import watchSaveSettings from './SaveSettings';

export default function* rootSaga() {
    yield [
        watchSaveSettings(),
    ];
}
