/**
* @author: Hao Qiao
*/

'use strict';

angular.module('mainApp',[
'ui.router',
'controller',
'directives'
])

.config(function($stateProvider, $urlRouterProvider) {

      $urlRouterProvider.otherwise("/home");

      $stateProvider.state('home',
            {
                  url: '/home',
                  templateUrl:'partials/home.html',
                  controller: 'home'
            });

      $stateProvider.state('productPreview',
            {
                  url: '/productPreview/:id',
                  templateUrl:'partials/productPreview.html',
                  controller: 'productPreview'
            });

      $stateProvider.state('adminProductPreview',
            {
                  url: '/adminProductPreview/:id',
                  templateUrl:'partials/adminProductPreview.html',
                  controller: 'adminProductPreview'
            });

      $stateProvider.state('product',
            {
                  url: '/product/:productID',
                  templateUrl:'partials/productDisplayPanel.html',
                  controller:'productDisplay'
            });
      $stateProvider.state('admin',
            {
                  url: '/admin',
                  templateUrl:'partials/adminConsole.html',
                  controller:'adminConsole'
            });

      $stateProvider.state('admin.search',
            {
                  url: '/search',
                  templateUrl:'partials/adminSearch.html',
                  controller:'adminSearch'
            });

      $stateProvider.state('submission',
            {
                  url: '/submissionPage',
                  templateUrl:'partials/submissionPage.html',
                  controller:'submission'
            });

      $stateProvider.state('user',
            {
                  url: '/userPage',
                  templateUrl:'partials/userPage.html',
                  controller:'user'
            });

      $stateProvider.state('productAdmin',
            {
                  url: '/productAdmin/{id}',
                  templateUrl:'partials/productAdminPage.html',
                  controller:'productAdmin'
            });

});