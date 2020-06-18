package space.votebot.bot.command.context

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*
import space.votebot.bot.constants.Embeds
import space.votebot.bot.dsl.EmbedConvention
import space.votebot.bot.util.EntityResolver

/**
 * A converter that converts a command argument.
 * @param T the type of the converted argument
 */
typealias ArgumentConverter<T> = (String) -> T

/**
 * Representation of a commands' arguments.
 * @param list the list of arguments
 * @property raw plain arguments string
 * @see List
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
data class Arguments(
        private val list: List<String>,
        private val raw: String
) :
        List<String> by list {

    /**
     * Joins the arguments back to their original String.
     */
    fun join(): String = raw

    /**
     * Split's the arguments by the specified [delimiter].
     * @see join
     * @return a new instance of [Arguments] containing the new split arguments
     */
    fun split(delimiter: String): Arguments = Arguments(raw.split(delimiter), raw)

    /**
     * Return the argument at the specified [index] or `null` if there is no argument at that position.
     */
    fun optionalArgument(index: Int): String? = getOrNull(index)

    /**
     * Return the argument at the specified [index] as an [Int] or `null` if there is no argument at that position, or it is not an [Int].
     */
    fun optionalInt(index: Int): Int? = optionalArgument(index, String::toIntOrNull)

    /**
     * Return the argument at the specified [index] as a [Long] or `null` if there is no argument at that position, or it is not a [Long].
     */
    fun optionalLong(index: Int): Long? = optionalArgument(index, String::toLongOrNull)

    /**
     * Return the argument at the specified [index] as a [User] or `null` if there is no argument at that position, or it is not a [User].
     * @param ignoreCase whether the case of the name should be ignored or not
     */
    suspend fun optionalUser(index: Int, ignoreCase: Boolean = false, jda: JDA): User? =
            optionalArgument(index) { EntityResolver.resolveUser(jda, it, ignoreCase) }

    /**
     * Return the argument at the specified [index] as a [Member] or `null` if there is no argument at that position, or it is not a [Member].
     * @param ignoreCase whether the case of the name should be ignored or not
     */
    suspend fun optionalMember(index: Int, ignoreCase: Boolean = false, guild: Guild): Member? =
            optionalArgument(index) { EntityResolver.resolveMember(guild, it, ignoreCase) }

    /**
     * Return the argument at the specified [index] as a [Role] or `null` if there is no argument at that position, or it is not a [Role].
     * @param ignoreCase whether the case of the name should be ignored or not
     */
    suspend fun optionalRole(index: Int, ignoreCase: Boolean = false, guild: Guild): Role? =
            optionalArgument(index) { EntityResolver.resolveRole(guild, it, ignoreCase) }

    /**
     * Return the argument at the specified [index] as a [T] or `null` if there is no argument at that position, or it is not a [T].
     */
    inline fun <reified T : Enum<T>> optionalEnum(index: Int): T? = optionalArgument(index) {
        java.lang.Enum.valueOf(T::class.java, it)
    }

    /**
     * Return the argument at the specified [index] as a [TextChannel] or `null` if there is no argument at that position, or it is not a [TextChannel].
     * @param ignoreCase whether the case of the name should be ignored or not
     */
    suspend fun optionalChannel(
            index: Int,
            ignoreCase: Boolean = false,
            guild: Guild
    ): TextChannel? =
            optionalArgument(index) { EntityResolver.resolveTextChannel(guild, it, ignoreCase) }

    /**
     * Return the argument at the specified [index] or `null` if there is no argument at that position.
     * And sends a command help if there is no argument at that position.
     * @param context the context that executed the command
     */
    fun requiredArgument(index: Int, context: Context): String? =
            requiredArgument(index, context, this::optionalArgument) {
                Embeds.command(context.command, context)
            }

    /**
     * Return the argument at the specified [index] as an [Int] or `null` if there is no argument at that position.
     * If there is no [Int] at that position it sends a help message.
     * @param context the context that executed the command
     */
    fun int(index: Int, context: Context): Int? = requiredArgument(index, context, this::optionalInt) {
        Embeds.error(
                "Invalid Number!",
                "Please enter a valid number."
        )
    }

    /**
     * Return the argument at the specified [index] as a [Long] or `null` if there is no argument at that position.
     * If there is no [Long] at that position it sends a help message.
     * @param context the context that executed the command
     */
    fun long(index: Int, context: Context): Long? = requiredArgument(index, context, this::optionalLong) {
        Embeds.error(
                "Invalid Number!",
                "Please enter a valid number."
        )
    }

    /**
     * Return the argument at the specified [index] as a [User] or `null` if there is no argument at that position.
     * If there is no [User] at that position it sends a help message.
     * @param ignoreCase whether the case of the name should be ignored or not
     * @param context the context that executed the command
     */
    suspend fun user(index: Int, ignoreCase: Boolean = false, context: Context): User? =
            requiredArgument(index, context, { optionalUser(index, ignoreCase, context.jda) }) {
                Embeds.error("Invalid User", "There seems to be no user with that name.")
            }

    /**
     * Return the argument at the specified [index] as a [Member] or `null` if there is no argument at that position.
     * If there is no [Member] at that position it sends a help message.
     * @param ignoreCase whether the case of the name should be ignored or not
     * @param context the context that executed the command
     */
    suspend fun member(index: Int, ignoreCase: Boolean = false, context: Context): Member? =
            requiredArgument(index, context, { optionalMember(index, ignoreCase, context.guild) }) {
                Embeds.error("Invalid User", "There seems to be no user with that name.")
            }

    /**
     * Return the argument at the specified [index] as a [Role] or `null` if there is no argument at that position.
     * If there is no [Role] at that position it sends a help message.
     * @param ignoreCase whether the case of the name should be ignored or not
     * @param context the context that executed the command
     */
    suspend fun role(index: Int, ignoreCase: Boolean = false, context: Context): Role? =
            requiredArgument(index, context, { optionalRole(index, ignoreCase, context.guild) }) {
                Embeds.error("Invalid Role!", "There seems to be no Role with that name.")
            }

    /**
     * Return the argument at the specified [index] as a [TextChannel] or `null` if there is no argument at that position.
     * If there is no [TextChannel] at that position it sends a help message.
     * @param ignoreCase whether the case of the name should be ignored or not
     * @param context the context that executed the command
     */
    suspend fun channel(index: Int, ignoreCase: Boolean = false, context: Context): TextChannel? =
            requiredArgument(index, context, { optionalChannel(index, ignoreCase, context.guild) }) {
                Embeds.error("Invalid Channel!", "There seems to be no channel with chat name.")
            }

    /**
     * Return the argument at the specified [index] as a [T] or `null` if there is no argument at that position.
     * If there is no [T] at that position it sends a help message.
     * @param context the context that executed the command
     */
    inline fun <reified T : Enum<T>> enum(index: Int, context: Context): T? = requiredArgument(index, context, {
        optionalEnum<T>(index)
    }, {
        Embeds.error("Invalid option!", "Please choose from one of the following options: `${T::class.java.enumConstants.joinToString("`", "`, `", "`")}`")
    })

    /**
     * Internal method.
     */
    inline fun <T> optionalArgument(index: Int, transform: ArgumentConverter<T>): T? =
            optionalArgument(index)?.let(transform)

    /**
     * Internal method.
     */
    inline fun <T> requiredArgument(
            index: Int,
            context: Context,
            provider: (Int) -> T?,
            lazyError: () -> EmbedConvention
    ): T? {
        val found = provider(index)
        if (found == null) context.respond(lazyError()).queue()
        return found
    }
}
