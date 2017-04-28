Equiv version number is inspired from [semver][]. The version number looks like
this:

    Major.Minor.Bugfix.Release

Major is increased when a significant rewrite of the application is done.

Minor is increased when a new feature has been added.

Bugfix is increased for a version which contains only bug fixes.

Release is increased each time a new build is published. This build does
not always reach all users, it goes like this:

0. Process starts with Release=0

1. When the code for version x.y.z.Release is considered ready to be tested,
   x.y.z.Release is built and published to testers.

2. Testers test the application.

3. If bugs are found, they get fixed, Release is increased and the process
   restarts from step 1.

4. If no bugs are found, x.y.z.Release is declared stable and published to all
   users.

This approach avoids the need of rebuilding a version just to remove a "beta"
label. When a build is declared stable, it gets published unaltered.

[semver]: http://semver.org/
