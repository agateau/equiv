# Create .apk

- Check current branch is master

- Check working tree is clean

- Bump version numbers:
    app/build.gradle versionCode
    app/build.gradle versionName

- Check translations are up to date

- Generate signed .apk

- Smoke test

- Update CHANGELOG.md

    Follow this format: <https://raw.githubusercontent.com/olivierlacan/keep-a-changelog/master/CHANGELOG.md>

- Commit

- Tag

    git tag -a $newv

- Push

    git push
    git push --tags

# Publish beta

- Upload to Google Play

    <https://play.google.com/apps/publish/?dev_acc=12107822308363902567#ApkPlace:p=com.agateau.equiv>

# Publish

- Take screenshots

- Publish beta version

- Write store changelog
