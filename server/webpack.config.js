const path = require('path');
const webpack = require('webpack');
const MiniCSSExtractPlugin = require('mini-css-extract-plugin');

module.exports = {
    mode: 'development',
    entry: {
        app: ['babel-polyfill', './src/app/index.js'],
    },
    output: {
        filename: '[name].js',
        path: path.resolve(__dirname, './src/app/dist'),
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /(node_modules)/,
                loader: 'babel-loader',
            },
            {
                test: /\.css$/,
                use: [
                    MiniCSSExtractPlugin.loader,
                    'css-loader',
                ],
            },
        ],
    },
    plugins: [
        new webpack.NoEmitOnErrorsPlugin(),
        new MiniCSSExtractPlugin({
            filename: '[name].css',
            chunkFilename: '[id].css',
        }),
    ],
};
