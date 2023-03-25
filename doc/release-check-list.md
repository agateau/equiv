# Create .apk

- [ ] Check current branch is master and working tree is clean

    git checkout master
    git pull
    git status
    git checkout -b prepare-release

- [ ] Bump version numbers (versionCode and versionName):

    vi version.properties

- [ ] Check translations are up to date

- [ ] Generate signed .apk

    make signed-apk

- Smoke test

    make test-signed-apk

- Update CHANGELOG.md. Follow this format: <https://raw.githubusercontent.com/olivierlacan/keep-a-changelog/master/CHANGELOG.md>

    vi CHANGELOG.md

- [ ] Commit

    git add .
    git commit -m "Preparing $VERSION"

- [ ] Push

    git push -u origin prepare-release
    gh pr create --fill
    gh pr merge --auto -dm

# Tag

- [ ] Tag

    git checkout master
    git pull
    git tag -a $VERSION -m "Release $VERSION"
    git push --tags
    git push -d origin prepare-release

# Publish beta

- [ ] Upload to Google Play

- [ ] Wait for Google Play to be happy

# Publish

- [ ] Take screenshots

- [ ] Publish beta version

- [ ] Write store changelog

- [ ] Create GitHub release
