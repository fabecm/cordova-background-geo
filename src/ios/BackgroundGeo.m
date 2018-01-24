//
//  BackgroundGeo.m
//  PluginLocationMyCampy
//
//  Created by CrispyBacon002 on 02/08/17.
//  Copyright © 2017 CrispyBacon002. All rights reserved.
//

#import "BackgroundGeo.h"
#import <CoreLocation/CoreLocation.h>



@implementation BackgroundGeo{
    CLLocationManager *locationManager;
    CDVInvokedUrlCommand *commandMain;
    NSMutableArray *points;
}

- (void)start:(CDVInvokedUrlCommand*)command
{
    points = [[NSMutableArray alloc] init];
    commandMain = command;
    // Create a location manager object
    locationManager = [[CLLocationManager alloc] init];
    locationManager.allowsBackgroundLocationUpdates = true;
    
    // Set the delegate
    locationManager.delegate = self;
    
    // Request location authorization
    [locationManager requestAlwaysAuthorization];
    
    // Start location updates
    [locationManager startUpdatingLocation];
}

- (void)stop:(CDVInvokedUrlCommand*)command
{
    if(locationManager)
        [locationManager stopUpdatingLocation];
    
    
}

- (void)getPoints:(CDVInvokedUrlCommand*)command
{
    commandMain = command;
    CDVPluginResult* pluginResult = nil;
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:points];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:commandMain.callbackId];
    
    
}

-(void)locationManager:(CLLocationManager *)manager
    didUpdateLocations:(NSArray *)locations {
    CLLocation * currentLocation = [locations lastObject];
    NSString * latitude = [NSString stringWithFormat:@"%.6f", currentLocation.coordinate.latitude];
    NSString * longitude = [NSString stringWithFormat:@"%.6f", currentLocation.coordinate.longitude];
    NSString * accuracy = [NSString stringWithFormat:@"%.6f", currentLocation.horizontalAccuracy];
    NSString * altitude = [NSString stringWithFormat:@"%.6f", currentLocation.altitude];
    int timestamp = [[NSDate date] timeIntervalSince1970];

    NSLog([NSString stringWithFormat:@"didUpdateLocations: %@, %@",latitude,longitude]);
    NSMutableDictionary *myDictionary = [[NSMutableDictionary alloc] init];
    [myDictionary setObject:[NSNumber numberWithDouble:[latitude doubleValue]] forKey:@"lat"];
    [myDictionary setObject:[NSNumber numberWithDouble:[longitude doubleValue]] forKey:@"long"];
    [myDictionary setObject:[NSNumber numberWithDouble:[accuracy doubleValue]] forKey:@"accuracy"];
    [myDictionary setObject:[NSNumber numberWithDouble:[altitude doubleValue]] forKey:@"altitude"];
    [myDictionary setObject:[NSNumber numberWithInt:timestamp] forKey:@"timestamp"];
    
    NSError * err;
    NSData * jsonData = [NSJSONSerialization dataWithJSONObject:myDictionary options:0 error:&err];
    NSString *myString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    CDVPluginResult* pluginResult = nil;
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:myDictionary];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:commandMain.callbackId];
    [points addObject:myDictionary];
}


@end
