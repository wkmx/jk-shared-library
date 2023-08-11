package cn.shoyoo.devops


def CheckOutCode(BranchName) {
    git branch: BranchName, credentialsId: 'cred', url: "{GIT_URL}"

    // dir("${env.workspace}"){
    //     env.GIT_COMMIT = sh(script: "git log -1 --pretty=%B | cat", returnStdout: true).trim()
    // }
}