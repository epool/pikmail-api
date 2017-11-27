package com.nearsoft.pikmail.api

import com.mixpanel.mixpanelapi.ClientDelivery
import com.mixpanel.mixpanelapi.MessageBuilder
import com.mixpanel.mixpanelapi.MixpanelAPI
import org.json.JSONObject


class AnalyticsManager {

    private val messageBuilder: MessageBuilder = MessageBuilder(System.getenv("MIXPANEL_TOKEN"))
    private val mixpanel = MixpanelAPI()

    fun trackSuccess(email: String, size: Int?, remoteIp: String?) = track(email, EventType.SUCCESS, "Size" to size, "RemoteIp" to remoteIp)

    fun trackError(email: String, throwable: Throwable) = track(email, EventType.ERROR, "Message" to throwable.message)

    private fun track(distinctId: String, eventType: EventType, vararg properties: Pair<String, Any?>) {
        val delivery = ClientDelivery().apply {
            addMessage(messageBuilder.set(distinctId, JSONObject(mapOf("\$email" to distinctId))))
            addMessage(messageBuilder.event(distinctId, eventType.name, JSONObject(mapOf(*properties))))
            addMessage(messageBuilder.increment(distinctId, mapOf("Counter" to 1L)))
        }
        mixpanel.deliver(delivery)
    }

    private enum class EventType { SUCCESS, ERROR }

}