
set qualitas_lib=/home/ewan/workspace/Qualitas/bin
set deploy_root=/home/ewan/programming/git/JavaAnalyserCore
set root=${deploy_root}/kalah_designs
set design=design1001
set kalahjarpath="../../../kalah_designs/lib/kalah20200717.jar"
set design_prefix=design
set location=/home/ewan/research/research-repository/code_corpora/grad007-01/grad007-01

set classpath=$qualitas_lib
set classpath=${classpath}:/opt/jars/commons-math3-3.6.1/commons-math3-3.6.1.jar
set classpath=${classpath}:../../../bin
set classpath=${classpath}:../../../lib/javaparser-core-3.23.0.jar
set classpath=${classpath}:../../../lib/javaparser-symbol-solver-core-3.23.0.jar
set classpath=${classpath}:../../../lib/failureaccess-1.0.1.jar
set classpath=${classpath}:../../../lib/guava-30.0-jre.jar
set classpath=${classpath}:../../../lib/javassist-3.27.0-GA.jar
set classpath=${classpath}:../../../lib/listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar
set classpath=${classpath}:${root}/kalah20200717.jar

set pushdsilent
pushd $location
set designs=`ls -1d $design_prefix*`
popd 
unset pushdsilent

set disabledesigns=(\
design1062\
)

foreach design ($designs)
#    echo $design
    java\
      -cp ${classpath}\
      com.qualitascorpus.javaanalyser.demo.JavaParserStatementCount\
      $location $design $kalahjarpath
    echo ""
end

#echo $location $design $kalahjarpath
