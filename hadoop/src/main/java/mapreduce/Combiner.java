package mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 按用户统计数量
 * 数据生成见{@link DataGenerate}
 */
public class Combiner {

    public static void main(String[] args) throws Exception {
        // 任务配置
        Job job = Job.getInstance(new Configuration());
        job.setJarByClass(Combiner.class);
        job.setMapperClass(WcMapper.class);
        job.setReducerClass(WcReducer.class);
        // 设置Combiner, 逻辑与Reduce相同
        job.setCombinerClass(WcReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Integer.class);
        // 输入输出
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        // 退出
        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }

    /**
     * Mapper
     */
    public static class WcMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        /**
         * 缓存
         */
        private static final IntWritable[] CACHE;
        static {
            CACHE = new IntWritable[DataGenerate.COUNT_MAX];
            for (int i = 0; i < CACHE.length; i++) {
                CACHE[i] = new IntWritable(i);
            }
        }

        /**
         * 每行执行一次
         */
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] s = line.split(" ");
            context.write(new Text(s[0]), CACHE[Integer.parseInt(s[2])]);
        }

    }

    /**
     * Reducer
     */
    public static class WcReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

        /**
         * 每key执行一次
         */
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value : values) {
                sum += value.get();
            }
            context.write(key, new IntWritable(sum));
        }

    }

}
