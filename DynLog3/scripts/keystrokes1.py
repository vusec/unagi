# This is a monkeyrunner jython script that opens a connection to an Android
# device and continually sends a stream of swipe and touch gestures.
#
# See http://developer.android.com/guide/developing/tools/monkeyrunner_concepts.html
#
# usage: monkeyrunner swipe_monkey.py
#

# Imports the monkeyrunner modules used by this program
from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice

# Connects to the current device
device = MonkeyRunner.waitForConnection()

device.type("abcdefg")
