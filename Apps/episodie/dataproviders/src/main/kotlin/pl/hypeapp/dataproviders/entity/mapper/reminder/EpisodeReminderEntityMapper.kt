package pl.hypeapp.dataproviders.entity.mapper.reminder

import pl.hypeapp.dataproviders.entity.mapper.Mapper
import pl.hypeapp.dataproviders.entity.room.EpisodeReminderEntity
import pl.hypeapp.domain.model.reminder.EpisodeReminderModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpisodeReminderEntityMapper @Inject constructor() : Mapper<EpisodeReminderEntity, EpisodeReminderModel>() {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun transform(model: EpisodeReminderModel?): EpisodeReminderEntity? {
        return EpisodeReminderEntity(episodeId = model?.episodeId!!,
                tvShowId = model.tvShowId,
                seasonId = model.seasonId,
                tvShowName = model.tvShowName,
                jobId = model.jobId,
                episodeNumber = model.episodeNumber,
                name = model.name,
                premiereDate = model.timestamp)
    }
}
