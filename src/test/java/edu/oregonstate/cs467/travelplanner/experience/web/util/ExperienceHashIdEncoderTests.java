package edu.oregonstate.cs467.travelplanner.experience.web.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

class ExperienceHashIdEncoderTests {
    @Test
    void encode_decode() {
        ExperienceHashIdEncoder encoder = new ExperienceHashIdEncoder("baptBDvk9ECYjNh5o2wA468z310Lfum", 8, 100);
        Random rand = new Random();

        List<Long> ids = Stream.generate(() -> rand.nextLong(1000000)).limit(10).toList();
        List<String> hashIds = ids.stream().map(encoder::encode).toList();
        List<Long> ids2 = hashIds.stream().map(encoder::decode).toList();
        Assertions.assertThat(ids2).isEqualTo(ids);
    }
}