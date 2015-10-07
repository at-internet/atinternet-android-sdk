/**
 * Created by rsalazard on 03/03/15.
 */

module.exports = function (grunt) {
    var alphaVersion = grunt.file.readJSON('core.manifest.json').version+'a';
    var betaVersion = grunt.file.readJSON('core.manifest.json').version+'b';
    var prodVersion = grunt.file.readJSON('core.manifest.json').version;

    // Project configuration.
    grunt.initConfig({
        /*
         * Delete work directories
         */
        clean: {
            all: ['delivery']
        },
        copy: {
            alphaLib: {
                expand: true,
                cwd: '../ATMobileAnalytics/Tracker/build/outputs/aar/Tracker.aar',
                src: '**',
                dest: 'delivery/' + alphaVersion + '/Tracker.aar'
            },
            alphaConfig: {
                expand: true,
                flatten: true,
                src: '../ATMobileAnalytics/Tracker/src/main/assets/defaultConfiguration.json',
                dest: 'delivery/' + alphaVersion
            },
            alphaDoc: {
                expand: true,
                flatten: true,
                src: 'index.html',
                dest: 'delivery/' + alphaVersion
            },
            alphaManifests: {
                expand: true,
                flatten: true,
                src: '*.manifest.json',
                dest: 'delivery/' + alphaVersion
            },
            betaLib: {
                expand: true,
                cwd: '../ATMobileAnalytics/Tracker/build/outputs/aar/Tracker.aar',
                src: '**',
                dest: 'delivery/' + betaVersion + '/Tracker.aar'
            },
            betaConfig: {
                expand: true,
                flatten: true,
                src: '../ATMobileAnalytics/Tracker/src/main/assets/defaultConfiguration.json',
                dest: 'delivery/' + betaVersion
            },
            betaDoc: {
                expand: true,
                flatten: true,
                src: 'index.html',
                dest: 'delivery/' + betaVersion
            },
            betaManifests: {
                expand: true,
                flatten: true,
                src: '*.manifest.json',
                dest: 'delivery/' + betaVersion
            },
            lib: {
                expand: true,
                cwd: '../ATMobileAnalytics/Tracker/build/outputs/aar/Tracker.aar',
                src: '**',
                dest: 'delivery/' + prodVersion + '/Tracker.aar'
            },
            config: {
                expand: true,
                flatten: true,
                src: '../ATMobileAnalytics/Tracker/src/main/assets/defaultConfiguration.json',
                dest: 'delivery/' + prodVersion
            },
            doc: {
                expand: true,
                flatten: true,
                src: 'index.html',
                dest: 'delivery/' + prodVersion
            },
            manifests: {
                expand: true,
                flatten: true,
                src: '*.manifest.json',
                dest: 'delivery/' + prodVersion
            }
        }
    });

    // These plugins provide necessary tasks.
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.registerTask('alphaDelivery', ['clean:all', 'copy:alphaLib','copy:alphaConfig', 'copy:alphaDoc','copy:alphaManifests']);
    grunt.registerTask('betaDelivery', ['clean:all', 'copy:betaLib', 'copy:betaConfig','copy:betaDoc','copy:betaManifests']);
    grunt.registerTask('delivery', ['clean:all', 'copy:lib', 'copy:config','copy:doc','copy:manifests']);
    grunt.registerTask('getVersion',function(){
        grunt.file.write('../version.txt', prodVersion);
    });
};
