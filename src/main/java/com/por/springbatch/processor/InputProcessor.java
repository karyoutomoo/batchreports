package com.por.springbatch.processor;

import com.por.springbatch.model.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class InputProcessor implements ItemProcessor<Data, Data> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputProcessor.class);

    @Override
    public Data process(Data data) throws Exception {
        LOGGER.info("Memproses input: {}", data);

        //set data dari input ke output
        Data output = new Data();
        output.setId(data.getId());
        output.setName(data.getName());

        LOGGER.info("Input yang sudah selesai di proses: {}", output);

        return output;
    }

}