//
//  BackgroundGeo.h
//  PluginLocationMyCampy
//
//  Created by CrispyBacon002 on 02/08/17.
//  Copyright © 2017 CrispyBacon002. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Cordova/CDVPlugin.h>

@interface BackgroundGeo : CDVPlugin

- (void)start:(CDVInvokedUrlCommand*)command;
- (void)stop:(CDVInvokedUrlCommand*)command;
- (void)getPoints:(CDVInvokedUrlCommand*)command;

@end
