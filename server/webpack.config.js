const path = require('path');
const webpack = require('webpack');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
  entry: {
      app: './src/app/app.js',
  },
  output: {
    filename: '[name].js',
    path: path.resolve(__dirname, './src/app/dist'),
  },
  module: {
    loaders: [
      {
        test: /\.js$/,
        exclude: /(node_modules)/,
        loader: 'babel-loader',
      },
      {
        test: /\.css$/,
        loader: ExtractTextPlugin.extract({ fallback: 'style-loader', use: ['css-loader?importLoaders=1', 'postcss-loader'] }),
      },
    ],
  },
  plugins: [
    new webpack.NoEmitOnErrorsPlugin(),
    new ExtractTextPlugin('[name].css')
  ],
};
