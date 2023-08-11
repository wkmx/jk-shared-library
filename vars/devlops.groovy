import cn.shoyoo.devops.GitServer


def start() {
    def gitServer = new GitServer()

    pipeline {
        // agent any

        options {
            disableConcurrentBuilds() // 禁止同时执行
            buildDiscarder(logRotator(daysToKeepStr: '7', numToKeepStr: '10')) // 构建记录保存7天,最多保存10个构建记录
            skipDefaultCheckout()
            timeout(time: 1, unit: 'HOURS')
            timestamps()
        }

        environment {
            GIT_URL: "github.com/wkmx/spider"
        }

        parameters {
                gitParameter (
                        defaultValue: 'master',
                        branchFilter: 'origin/(.*)',
                        name: 'BRANCH_NAME',
                        quickFilterEnabled: false,
                        selectedValue: 'DEFAULT',
                        sortMode: 'DESCENDING_SMART',
                        tagFilter: '*',
                        type: 'GitParameterDefinition',
                        description: '选择分支默认，是当前环境分支',
                        useRepository: GIT_URL
                )
            }



        stages {
            stage('获取用户名') {
                steps {
                    wrap([$class: 'BuildUser']) {
                        script {
                            env.BUILD_USER = "${env.BUILD_USER}"
                        }
                    }
                }
            }

            stage('Clean up workspace') {
                steps {
                    script {
                        cleanWs()
                    }
                }
            }

            stage('checkout from scm') {
                steps {
                    script {
                        println("------ 拉取代码并获取git log ------")
                        print("${params.BRANCH_NAME}")
                        print("${env}")
                        GitServer.CheckOutCode("${params.BRANCH_NAME}")
                    }
                }
            }

        }

    }
}