package com.nearsoft.pikmail.api

import com.mixpanel.mixpanelapi.ClientDelivery
import com.mixpanel.mixpanelapi.MessageBuilder
import com.mixpanel.mixpanelapi.MixpanelAPI
import org.json.JSONObject


class AnalyticsManager {

    private val messageBuilder: MessageBuilder = MessageBuilder(System.getenv("MIXPANEL_TOKEN"))
    private val mixpanel = MixpanelAPI()

    fun trackSuccess(email: String, remoteIp: String?, size: Int?) = track(email, EventType.SUCCESS, remoteIp, "Size" to size)

    fun trackError(email: String, remoteIp: String?, throwable: Throwable) = track(email, EventType.ERROR, remoteIp, "Message" to throwable.message)

    private fun track(distinctId: String, eventType: EventType, remoteIp: String?, vararg properties: Pair<String, Any?>) {
        val delivery = ClientDelivery().apply {
            addMessage(messageBuilder.set(distinctId, JSONObject(mapOf("\$email" to distinctId))))
            addMessage(messageBuilder.event(distinctId, eventType.name, JSONObject(mapOf(*properties, "RemoteIp" to remoteIp))))
            addMessage(messageBuilder.increment(distinctId, mapOf("Counter" to 1L)))
        }
        mixpanel.deliver(delivery)
    }

    private enum class EventType { SUCCESS, ERROR }

}