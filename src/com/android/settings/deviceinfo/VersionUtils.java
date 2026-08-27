
package com.android.settings.deviceinfo;

import android.os.SystemProperties;

public class VersionUtils {
  public static String getCustomSystemVersion() {
    return SystemProperties.get("ro.custom.system.version", "");
  }

  public static String getCustomVersion() {
    return SystemProperties.get("ro.custom.version", "");
  }
}
