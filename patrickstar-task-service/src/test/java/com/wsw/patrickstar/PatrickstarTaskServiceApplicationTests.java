package com.wsw.patrickstar;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wsw.patrickstar.entity.Task;
import com.wsw.patrickstar.entity.TaskMessage;
import com.wsw.patrickstar.enums.TopicEnum;
import com.wsw.patrickstar.mapper.TaskMapper;
import com.wsw.patrickstar.message.KafkaMessageService;
import com.wsw.patrickstar.util.MinioUtil;
import org.apache.commons.collections4.MapUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
class PatrickstarTaskServiceApplicationTests {
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private KafkaMessageService kafkaMessageService;

    private final String bucketName = "wsw";

    public void selectTask(char taskStatus) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_status", taskStatus);
        List<Map<String, Object>> taskMapList = taskMapper.selectMaps(queryWrapper);
        for (Map<String, Object> taskMap : taskMapList) {
            String taskName = MapUtils.getString(taskMap, "task_name", "");
            System.out.println(taskName);
        }
    }

    @Test
    void contextLoads() {
        selectTask('0');
    }

    @Test
    void uploadFile() {
        String fileName = "667048448726.PDF";
        String filePath = "C:\\Users\\wangsongwen\\Desktop\\667048448726.PDF";
        minioUtil.upload(bucketName, fileName, filePath);
    }

    @Test
    void kafkaSendMessage() {
        TaskMessage message = new TaskMessage();
        message.setTaskId(2L);
        message.setMessage("Hello World");
        kafkaMessageService.sendMessage(TopicEnum.TASK_TO_TASK, message);
    }
}
