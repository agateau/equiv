# Shell to use, stop on errors, stop on undefined variables, report errors
# if a command in a pipe fails (not just the last)
SHELL := bash
.SHELLFLAGS := -euo pipefail -c

# Do not start a new shell for each command of a target
# Makes it possible to have `cd foo` on its own line. Be sure to configure the
# shell to stop on errors though (the -e in .SHELLFLAGS)
.ONESHELL:

MAKEFLAGS += --warn-undefined-variables
MAKEFLAGS += --no-builtin-rules

include version.properties

ARCHIVE_DIR=$(CURDIR)/archives

ASSETS_DIR=app/src/main/assets

CSV=$(ASSETS_DIR)/products.csv

APK_NAME=equiv-$(VERSION).apk
ANDROID_PACKAGE_NAME=com.agateau.equiv

GRADLEW=./gradlew
ifdef OFFLINE
	GRADLEW=./gradlew --offline
endif

all: $(CSV)

$(CSV): products.ods bin/ods2csv.py
	bin/ods2csv.py --skip-row 2 --skip-column 6 products.ods $(ASSETS_DIR)

clean:
	rm -f $(CSV)

signed-apk:
	@echo Creating apk file
	@$(GRADLEW) assembleRelease
	@echo Moving apk file
	@mkdir -p $(ARCHIVE_DIR)
	mv app/build/outputs/apk/release/app-release.apk $(ARCHIVE_DIR)/$(APK_NAME)

test-signed-apk:
	# uninstall any existing version in case we have an unsigned version installed
	adb uninstall $(ANDROID_PACKAGE_NAME) || true
	adb install -f $(ARCHIVE_DIR)/$(APK_NAME)
	adb shell am start -n $(ANDROID_PACKAGE_NAME)/com.agateau.equiv.ui.MainActivity
