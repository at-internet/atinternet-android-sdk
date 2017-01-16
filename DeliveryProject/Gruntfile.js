/**
 * Created by rsalazard on 03/03/15.
 */

module.exports = function (grunt) {
    var alphaVersion = grunt.file.readJSON('core.manifest.json').version + 'a';
    var prodVersion = grunt.file.readJSON('core.manifest.json').version;

    function getVersion() {
        grunt.file.write('../version.txt', prodVersion);
    }

    // Project configuration.
    grunt.initConfig({
        /*
         * Delete work directories
         */
        clean: {
            all: ['delivery']
        },
        copy: {
            alphaPackage: {
                expand: true,
                cwd: '../ATMobileAnalytics/Tracker/build/intermediates/bundles/release',
                src: '**',
                dest: 'delivery/' + alphaVersion + '/Tracker.aar'
            },
            alphaRes: {
                expand: true,
                flatten: true,
                src: ['../ATMobileAnalytics/Tracker/src/main/assets/defaultConfiguration.json', 'index.html', '*.json'],
                dest: 'delivery/' + alphaVersion
            },
            prodPackage: {
                expand: true,
                cwd: '../ATMobileAnalytics/Tracker/build/intermediates/bundles/release',
                src: '**',
                dest: 'delivery/' + prodVersion + '/Tracker.aar'
            },
            prodRes: {
                expand: true,
                flatten: true,
                src: ['../ATMobileAnalytics/Tracker/src/main/assets/defaultConfiguration.json', 'index.html', '*.json'],
                dest: 'delivery/' + prodVersion
            }
        }
    });

    // These plugins provide necessary tasks.
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.registerTask('alphaDelivery', ['clean:all', 'copy:alphaPackage', 'copy:alphaRes']);
    grunt.registerTask('prodDelivery', ['clean:all', 'copy:prodPackage', 'copy:prodRes']);
    grunt.registerTask('getVersion', getVersion);
};
